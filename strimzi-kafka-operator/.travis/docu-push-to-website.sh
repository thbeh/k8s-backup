#!/usr/bin/env bash

set -e

openssl aes-256-cbc -K $encrypted_c563b2a48eea_key -iv $encrypted_c563b2a48eea_iv -in .travis/github_deploy_key.enc -out github_deploy_key -d
chmod 600 github_deploy_key
eval `ssh-agent -s`
ssh-add github_deploy_key

git clone git@github.com:strimzi/strimzi.github.io.git /tmp/website
cp -v documentation/htmlnoheader/master.html /tmp/website/docs/master/master.html
cp -v documentation/html/index.html /tmp/website/docs/master/full.html
cp -v documentation/htmlnoheader/contributing.html /tmp/website/contributing/guide/contributing.html
cp -v documentation/html/contributing.html /tmp/website/contributing/guide/full.html
rm -rf /tmp/website/docs/master/images
rm -rf /tmp/website/contributing/guide/images
rm -rf /tmp/website/contributing/guide/templates
cp -vrL documentation/htmlnoheader/images /tmp/website/docs/master/images
cp -vrL documentation/htmlnoheader/images /tmp/website/contributing/guide/images
cp -vrL documentation/contributing/templates /tmp/website/contributing/guide/templates

pushd /tmp/website

if [[ -z $(git status -s) ]]; then
    echo "No changes to the output on this push; exiting."
    exit 0
fi

git config user.name "Travis CI"
git config user.email "ci@travis.tld"

git add -A
git commit -m "Update documentation (Travis CI build ${TRAVIS_BUILD_NUMBER})" --allow-empty
git push origin master

popd
