# Service Mesh demo for Red Hat Forum Zurich 2019 

This repository contains instructions for running service mesh demo for Red Hat Forum in Zurich.
It is bookinfo from Istio upstream with modified reviews application deployed on Quarkus.

## Install

### Provision OCP on AWS
```bash
./openshift-install create install-config --dir ${PWD}
./openshift-install create cluster --dir=${PWD}
export KUBECONFIG=${HOME}/bin/aws/auth/kubeconfig
```

The kubeadmin password is in `.openshift_install.log`

#### Create users, with HTTP auth in OCP
```bash
htpasswd -b -c users.httppasswd kiali <password>
htpasswd -b users.httppasswd jdoe <password>
oc create secret generic htpasswd --from-file=htpasswd=users.httppasswd -n openshift-config --dry-run=true -o yaml

oc apply -f manifests/oauth.yaml
oc adm policy add-cluster-role-to-user cluster-admin kiali
```

### Login
```bash
oc login https://api.ploffay.crossproduct.rhmw.io:6443 -u kiali|kubeadmin -p 
```

### Deploy Jaeger, Elasticsearch, Istio operators
Follow the instructions on https://gitlab.cee.redhat.com/istio/install-rhossm/tree/master

### Create Istio Service Mesh
```bash
oc new-project istio-system
oc create -f manifests/smcp.yaml # oc delete smcp full-install
# oc edit kiali kiali  -n istio-system # change tracing.service: "jaeger-query"
```

## Deploy Bookinfo
```bash
oc new-project bookinfo
oc -n bookinfo apply -f manifests/00-product-details-reviewsv1.yaml && oc -n bookinfo apply -f manifests/bookinfo-gateway.yaml
oc -n bookinfo apply -f manifests/01-reviewsv2-ratings.yaml
oc -n bookinfo apply -f manifests/02-reviewsv3.yaml
```

Do request to the app:
```bash
export GATEWAY_URL=$(oc -n istio-system get route istio-ingressgateway -o jsonpath='{.spec.host}')
echo http://${GATEWAY_URL}/productpage
```

### Remove Bookinfo
```bash
oc delete project bookinfo
```

## Develop Reviews application
```bash
oc port-forward pod/ratings-v1-d4db56d5b-bpl9g 9081:9080 -n bookinfo
ENABLE_REvIEWS=true make dev
curl -ivX GET localhost:9080/reviews/0
```

### Build ProductPage application
The application is not hosted here. The modifications were done in a separate repository.

```bash
docker build -t "quay.io/pavolloffay/examples-bookinfo-productpage-v1:0.12.0" .
docker push quay.io/pavolloffay/examples-bookinfo-productpage-v1:0.12.0
``` 
