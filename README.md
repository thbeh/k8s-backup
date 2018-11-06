###### dashboard
> kubectl --kubeconfig .kube/config proxy --address='0.0.0.0' --accept-hosts='^*$'

###### grafana
> kubectl --kubeconfig .kube/config port-forward $(kubectl --kubeconfig .kube/config get pods --selector=app=kube-prometheus-grafana -n  monitoring --output=jsonpath="{.items..metadata.name}") -n monitoring 3000

###### Installing Helm
- [x] kubectl create -f tiller-rbac-config.yaml
- [x] curl https://raw.githubusercontent.com/kubernetes/helm/master/scripts/get > get_helm.sh
- [x] chmod 0700 get_helm.sh
- [x] ./get_helm.sh
- [x] helm init --service-account tiller
- [ ] \(Optional) kubectl get pods -l name=tiller -n kube-system | grep ContainerCreating > /dev/null;

###### Installing Prometheus Charts
- [x] helm repo add coreos https://s3-eu-west-1.amazonaws.com/coreos-charts/stable/
- [x] helm install coreos/prometheus-operator --name prometheus-operator --namespace monitoring
- [x] helm install coreos/kube-prometheus --name kube-prometheus --set global.rbacEnable=true --namespace monitoring

###### Installing Metallb
- [x] kubectl --kubeconfig .kube/config apply -f metallb.yaml
- [x] kubectl --kubeconfig .kube/config apply -f example-layer2-config.yaml

###### Installing GlusterFS and Heketi
- [x] cd deploy
- [x] vi topology.json 
- [x] ./gk-deploy -g  
- [x] wget https://github.com/heketi/heketi/reldashboardeases/download/v7.0.0/heketi-client-v7.0.0.linux.amd64.tar.gz
- [x] tar zxvf heketi-client-v7.0.0.linux.amd64.tar.gz
- [x] cp heketi-client/bin/heketi-cli /usr/local/bin/

> export HEKETI_CLI_SERVER=http://10.244.3.7:8080 <- deploy-heketi sever
> export HEKETI_CLI_SERVER=$(kubectl get svc/deploy-heketi --template 'http://{{.spec.clusterIP}}:{{(index .spec.ports 0).port}}')
\(Optional) heketi-cli volume list
\(Optional) heketi-cli topology info

###### Installing Minio
- [x] vi minio-storage-class.yaml (edit heketi endpoint)
- [x] kubectl create -f minio-sc.yaml
- [x] kubectl create -f minio-distributed-headless-service.yaml
- [x] kubectl create -f minio-distributed-statefulset.yaml (not sure why 50G does not work)
- [x] kubectl create -f minio-loadbalance-service.yaml (expose to etxernal IP via metallb)

###### Installing awscli
- [x] pip install awscli --upgrade --user (in home directory e.g. /home/thbeh)
- [x] .local/bin/aws --version
- [x] .local/bin/aws configure
- [x] .local/bin/aws configure set default.s3.signature_version s3v4
- [x] .local/bin/aws --endpoint-url http://192.168.56.201:9000 s3 ls
- [x] .local/bin/aws --endpoint-url http://192.168.56.201:9000 s3 ls s3://thbeh

###### Installing Strimzi 0.8.0
- [x] sed -i 's/namespace: .*/namespace: default/' examples/install/cluster-operator/*ClusterRoleBinding*.yaml
- [x] kubectl create -f examples/install/cluster-operator
- [x] vi kafka-sc.yaml (copy from minio-sc.yaml, use kafka-cluster as storage class name)
- [x] kubectl create -f kafka-sc.yaml
- [x] kubectl apply -f examples/kafka/kafka-persistent.yaml (check persistence, currently 5Gi)

###### cd strimzi-0.5.0
- [x] kubectl create -f mysql-sc.yaml
- [x] kubectl --kubeconfig ../.kube/config  create -f mysql.yaml 
- [x] kubectl --kubeconfig ../.kube/config  exec -it wordpress-mysql-55ffb8f7d5-k6ns8 bash \(create tables in inventory db, e.g. source /docker-entrypoint-initdb.d/inventory.sql )
- [x] kubectl --kubeconfig ../.kube/config  create -f kafka-connect.yaml

###### Register connector
- [x] vi register.json
> cat register.json | kubectl exec -i my-cluster-kafka-0 -- curl -s -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://my-connect-cluster-connect-api:8083/connectors -d @-
> kubectl exec -i my-cluster-kafka-0 -- curl -s -X GET -H "Content-Type:application/json" http://my-connect-cluster-connect-api:8083/connectors/inventory-connector/status | jq
> kubectl --kubeconfig ../.kube/config exec  -i my-cluster-zookeeper-0 -- bin/kafka-topics.sh --zookeeper localhost:21810 --list

> export MYSQLPOD=$(kubectl --kubeconfig .kube/config get pods -l app=wordpress --no-headers | awk '{print $1}')
kubectl --kubeconfig .kube/config exec -ti $MYSQLPOD -- mysql --user=debezium --password=dbz

> kubectl exec -i my-cluster-kafka-0 -- curl -s -X GET -H "Content-Type:application/json" http://my-connect-cluster-connect-api:8083/connectors/inventory-connector/status | jq
> kubectl exec -i my-cluster-kafka-0 -- /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic dbserver1.inventory.products --from-beginning --max-messages 4 | jq

###### Installing Snappydata charts
- [x] kubectl create -f k8s-spark-rbac.yaml
- [x] create bucket - s3a:///thbeh (and upload a empty file to bucket)
- [x] kubectl create -f spark-history-server.yaml
- [x] kubectl create -f snappydata-sc.yaml
- [x] helm install --name snappydata --namespace snappy k8s-master-backup_24_09_2018/spark-on-k8s/charts/snappydata/
- [x] kubectl create -f ../spark-sc.yaml
- [x] helm install --name zeppelin --namespace spark charts/zeppelin-with-spark/
- [x] helm install --name spark-rss --namespace spark charts/spark-rss/

~~kubectl create clusterrolebinding spark-role --clusterrole=edit --serviceaccount=default:spark --namespace=default~~

###### Running Spark
- [x] sudo yum install java-1.8.0-openjdk-devel
- [x] export JAVA_HOME=/usr/lib/jvm/java
- [x] export HADOOP_HOME=/opt/vagrant/projects/hadoop-2.7.3
- [x] export PATH=$PATH:$HADOOP_HOME/bin
- [x] export SPARK_DIST_CLASSPATH=$(hadoop classpath)
