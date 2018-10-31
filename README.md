###dashboard
kubectl --kubeconfig .kube/config proxy --address='0.0.0.0' --accept-hosts='^*$'

grafana
kubectl --kubeconfig .kube/config port-forward $(kubectl --kubeconfig .kube/config get pods --selector=app=kube-prometheus-grafana -n  monitoring --output=jsonpath="{.items..metadata.name}") -n monitoring 3000

kubectl create -f tiller-rbac-config.yaml

curl https://raw.githubusercontent.com/kubernetes/helm/master/scripts/get > get_helm.sh
chmod 0700 get_helm.sh
./get_helm.sh

helm init --service-account tiller

kubectl get pods -l name=tiller -n kube-system | grep ContainerCreating > /dev/null;

helm repo add coreos https://s3-eu-west-1.amazonaws.com/coreos-charts/stable/
helm install coreos/prometheus-operator --name prometheus-operator --namespace monitoring
helm install coreos/kube-prometheus --name kube-prometheus --set global.rbacEnable=true --namespace monitoring


kubectl --kubeconfig .kube/config apply -f metallb.yaml
kubectl --kubeconfig .kube/config apply -f example-layer2-config.yaml

cd deploy
vi topology.json 
./gk-deploy -g  

wget https://github.com/heketi/heketi/releases/download/v7.0.0/heketi-client-v7.0.0.linux.amd64.tar.gz
tar zxvf heketi-client-v7.0.0.linux.amd64.tar.gz
cp heketi-client/bin/heketi-cli /usr/local/bin/

export HEKETI_CLI_SERVER=http://10.244.3.7:8080 <- deploy-heketi sever
export HEKETI_CLI_SERVER=$(kubectl get svc/deploy-heketi --template 'http://{{.spec.clusterIP}}:{{(index .spec.ports 0).port}}')
heketi-cli volume list
heketi-cli topology info

vi minio-storage-class.yaml (edit heketi endpoint)
kubectl create -f minio-sc.yaml
kubectl create -f minio-distributed-headless-service.yaml
kubectl create -f minio-distributed-statefulset.yaml (not sure why 50G does not work)
kubectl create -f minio-loadbalance-service.yaml (expose to etxernal IP via metallb)

pip install awscli --upgrade --user (in home directory e.g. /home/thbeh)
.local/bin/aws --version
.local/bin/aws configure
.local/bin/aws configure set default.s3.signature_version s3v4
.local/bin/aws --endpoint-url http://192.168.56.201:9000 s3 ls
.local/bin/aws --endpoint-url http://192.168.56.201:9000 s3 ls s3://thbeh

sed -i 's/namespace: .*/namespace: default/' examples/install/cluster-operator/*ClusterRoleBinding*.yaml
kubectl create -f examples/install/cluster-operator
vi kafka-sc.yaml (copy from minio-sc.yaml, use kafka-cluster as storage class name)
kubectl create -f kafka-sc.yaml

kubectl apply -f examples/kafka/kafka-persistent.yaml (check persistence, currently 5Gi)

cd strimzi-0.5.0
kubectl create -f mysql-sc.yaml
kubectl --kubeconfig ../.kube/config  create -f mysql.yaml 
kubectl --kubeconfig ../.kube/config  exec -it wordpress-mysql-55ffb8f7d5-k6ns8 bash (create tables in inventory db, e.g. source /docker-entrypoint-initdb.d/inventory.sql )

kubectl --kubeconfig ../.kube/config  create -f kafka-connect.yaml
vi register.json
cat register.json | kubectl exec -i my-cluster-kafka-0 -- curl -s -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://my-connect-cluster-connect-api:8083/connectors -d @-
kubectl exec -i my-cluster-kafka-0 -- curl -s -X GET -H "Content-Type:application/json" http://my-connect-cluster-connect-api:8083/connectors/inventory-connector/status | jq
kubectl --kubeconfig ../.kube/config exec  -i my-cluster-zookeeper-0 -- bin/kafka-topics.sh --zookeeper localhost:21810 --list

export MYSQLPOD=$(kubectl --kubeconfig .kube/config get pods -l app=wordpress --no-headers | awk '{print $1}')
kubectl --kubeconfig .kube/config exec -ti $MYSQLPOD -- mysql --user=debezium --password=dbz

kubectl exec -i my-cluster-kafka-0 -- curl -s -X GET -H "Content-Type:application/json" http://my-connect-cluster-connect-api:8083/connectors/inventory-connector/status | jq
kubectl exec -i my-cluster-kafka-0 -- /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic dbserver1.inventory.products --from-beginning --max-messages 4 | jq


kubectl create -f k8s-spark-rbac.yaml
create bucket - s3a:///thbeh (and upload a empty file to bucket)
kubectl create -f spark-history-server.yaml
kubectl create -f snappydata-sc.yaml

helm install --name snappydata --namespace snappy k8s-master-backup_24_09_2018/spark-on-k8s/charts/snappydata/
kubectl create -f ../spark-sc.yaml
helm install --name zeppelin --namespace spark charts/zeppelin-with-spark/
helm install --name spark-rss --namespace spark charts/spark-rss/

kubectl create clusterrolebinding spark-role --clusterrole=edit --serviceaccount=default:spark --namespace=default

sudo yum install java-1.8.0-openjdk-devel
export JAVA_HOME=/usr/lib/jvm/java
export HADOOP_HOME=/opt/vagrant/projects/hadoop-2.7.3
export PATH=$PATH:$HADOOP_HOME/bin
export SPARK_DIST_CLASSPATH=$(hadoop classpath)
