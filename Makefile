
DOCKER_NAMESPACE?=quay.io/pavolloffay
VERSION?=0.12.0

.PHONY:build
build:
	cd quarkus-istio && ./mvnw clean package

.PHONY:dev
dev:
	cd quarkus-istio && QUARKUS_JAEGER_ENDPOINT=http://localhost:14268/api/traces ./mvnw compile quarkus:dev -Dquarkus.http.port=9080

.PHONY: all
all: build v1 v2 v3

.PHONY: v1
v1: build
	docker build -t "${DOCKER_NAMESPACE}/examples-bookinfo-reviews-v1:${VERSION}" \
	--build-arg service_version=v1 ./quarkus-istio
	docker push ${DOCKER_NAMESPACE}/examples-bookinfo-reviews-v1:${VERSION}

.PHONY: v2
v2: build
	docker build -t "${DOCKER_NAMESPACE}/examples-bookinfo-reviews-v2:${VERSION}" \
	--build-arg service_version=v2 --build-arg enable_ratings=true ./quarkus-istio
	docker push ${DOCKER_NAMESPACE}/examples-bookinfo-reviews-v2:${VERSION}

.PHONY: v3
v3: build
	docker build -t "${DOCKER_NAMESPACE}/examples-bookinfo-reviews-v3:${VERSION}" \
	--build-arg service_version=v3 --build-arg enable_ratings=true --build-arg star_color=red ./quarkus-istio
	docker push ${DOCKER_NAMESPACE}/examples-bookinfo-reviews-v3:${VERSION}

.PHONY: redeploy-v3
redeploy-v3:
	@kubectl patch deployment reviews-v3 -p  "{\"spec\":{\"template\":{\"metadata\":{\"annotations\":{\"date\":\"`date +'%s'`\"}}}}}" -n bookinfo
