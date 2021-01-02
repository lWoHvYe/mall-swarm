#! /usr/bin/python3

import os

images = [
    "kube-apiserver:v1.20.1",
    "kube-controller-manager:v1.20.1",
    "kube-scheduler:v1.20.1",
    "kube-proxy:v1.20.1",
    "pause:3.2",
    "etcd:3.4.13-0",
    "coredns:1.7.0",
]

for i in images:
    pullCMD = "docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/{}".format(i)
    print("run cmd '{}', please wait ...".format(pullCMD))
    os.system(pullCMD)

    tagCMD = "docker tag registry.cn-hangzhou.aliyuncs.com/google_containers/{} k8s.gcr.io/{}".format(i, i)
    print("run cmd '{}', please wait ...".format(tagCMD))
    os.system(tagCMD)

    rmiCMD = "docker rmi registry.cn-hangzhou.aliyuncs.com/google_containers/{}".format(i)
    print("run cmd '{}', please wait ...".format(rmiCMD))
    os.system(rmiCMD)
