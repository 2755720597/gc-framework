## Kubernetes Architecture的不同组件有哪些？
Kubernetes Architecture主要有两个组件 - 主节点和工作节点。如下图所示，master和worker节点中包含许多内置组件。
主节点具有kube-controller-manager，kube-apiserver，kube-scheduler等。而工作节点具有在每个节点上运行的kubelet和kube-proxy。

![](https://img-blog.csdnimg.cn/img_convert/154ce3bf6016e9b399803b5b6a67c29a.png)

## 你对Kube-proxy有什么了解？
Kube-proxy可以在每个节点上运行，并且可以跨后端网络服务进行简单的TCP / UDP数据包转发。基本上，它是一个网络代理，
它反映了每个节点上Kubernetes API中配置的服务。因此，Docker可链接的兼容环境变量提供由代理打开的群集IP和端口。

## 您能否介绍一下Kubernetes中主节点的工作情况？
Kubernetes master控制容器存在的节点和节点内部。现在，这些单独的容器包含在容器内部和每个容器内部，
您可以根据配置和要求拥有不同数量的容器。因此，如果必须部署pod，则可以使用用户界面或命令行界面部署它们。然后，
在节点上调度这些pod，并根据资源需求，将pod分配给这些节点。kube-apiserver确保在Kubernetes节点和主组件之间建立通信。

![](https://img-blog.csdnimg.cn/img_convert/31f74502a96b958fd01884443060e815.png)

## kube-apiserver和kube-scheduler的作用是什么？
kube -apiserver遵循横向扩展架构，是主节点控制面板的前端。这将公开Kubernetes主节点组件的所有API，
并负责在Kubernetes节点和Kubernetes主组件之间建立通信。

kube-scheduler负责工作节点上工作负载的分配和管理。因此，它根据资源需求选择最合适的节点来运行未调度的pod，
并跟踪资源利用率。它确保不在已满的节点上调度工作负载。

## 你能简要介绍一下Kubernetes控制管理器吗？
多个控制器进程在主节点上运行，但是一起编译为单个进程运行，即Kubernetes控制器管理器。
因此，Controller Manager是一个嵌入控制器并执行命名空间创建和垃圾收集的守护程序。
它拥有责任并与API服务器通信以管理端点。

因此，主节点上运行的不同类型的控制器管理器是：

![](https://img-blog.csdnimg.cn/img_convert/344c75e0ea8ed77fbe4970804a263a30.png)

## 什么是ETCD？
Etcd是用Go编程语言编写的，是一个分布式键值存储，用于协调分布式工作。因此，Etcd存储Kubernetes集群的配置数据，
表示在任何给定时间点的集群状态。

## Kubernetes有哪些不同类型的服务？
以下是使用的不同类型的服务：

![](https://img-blog.csdnimg.cn/img_convert/05717a2e9bc85018d86724d5e58e77ec.png)

## 你对Kubernetes的负载均衡器有什么了解？
负载均衡器是暴露服务的最常见和标准方式之一。根据工作环境使用两种类型的负载均衡器，即内部负载均衡器或外部负载均衡器。
内部负载均衡器自动平衡负载并使用所需配置分配容器，而外部负载均衡器将流量从外部负载引导至后端容器。

## 什么是Ingress网络，它是如何工作的？
Ingress网络是一组规则，充当Kubernetes集群的入口点。这允许入站连接，可以将其配置为通过可访问的URL，
负载平衡流量或通过提供基于名称的虚拟主机从外部提供服务。因此，Ingress是一个API对象，
通常通过HTTP管理集群中服务的外部访问，是暴露服务的最有效方式。

现在，让我以一个例子向您解释Ingress网络的工作。

有2个节点具有带有Linux桥接器的pod和根网络命名空间。除此之外，
还有一个名为flannel0（网络插件）的新虚拟以太网设备被添加到根网络中。

现在，假设我们希望数据包从pod1流向pod 4.请参阅下图。

![](https://img-blog.csdnimg.cn/img_convert/78c2f95110755d3a0886336210c7fed0.png)

* 因此，数据包将pod1的网络保留在eth0，并进入veth0的根网络。

* 然后它被传递给cbr0，这使得ARP请求找到目的地，并且发现该节点上没有人具有目的地IP地址。

* 因此，桥接器将数据包发送到flannel0，因为节点的路由表配置了flannel0。

* 现在，flannel守护程序与Kubernetes的API服务器通信，以了解所有pod IP及其各自的节点，以创建pods IP到节点IP的映射。

* 网络插件将此数据包封装在UDP数据包中，其中额外的标头将源和目标IP更改为各自的节点，并通过eth0发送此数据包。

* 现在，由于路由表已经知道如何在节点之间路由流量，因此它将数据包发送到目标节点2。

* 数据包到达node2的eth0并返回到flannel0以解封装并在根网络命名空间中将其发回。

* 同样，数据包被转发到Linux网桥以发出ARP请求以找出属于veth1的IP。

* 数据包最终穿过根网络并到达目标Pod4。

## 您对云控制器管理器有何了解？
Cloud Controller Manager负责持久存储，网络路由，从核心Kubernetes特定代码中抽象出特定于云的代码，
以及管理与底层云服务的通信。它可能会分成几个不同的容器，具体取决于您运行的是哪个云平台，
然后它可以使云供应商和Kubernetes代码在没有任何相互依赖的情况下开发。
因此，云供应商开发他们的代码并在运行Kubernetes时与Kubernetes云控制器管理器连接。

各种类型的云控制器管理器如下：

![](https://img-blog.csdnimg.cn/img_convert/720d07a44e7f0d116b920dc6f80589cd.png)

## 什么是Container资源监控？
对于用户而言，了解应用程序的性能和所有不同抽象层的资源利用率非常重要，Kubernetes通过在容器，pod，
服务和整个集群等不同级别创建抽象来考虑集群的管理。现在，可以监视每个级别，这只是容器资源监视。

各种容器资源监控工具如下：

![](https://img-blog.csdnimg.cn/img_convert/12565d6925603b9d3250a5b73167a34d.png)

## Replica Set 和 Replication Controller之间有什么区别？
Replica Set 和 Replication Controller几乎完全相同。它们都确保在任何给定时间运行指定数量的pod副本。
不同之处在于复制pod使用的选择器。Replica Set使用基于集合的选择器，而Replication Controller使用基于权限的选择器。

* Equity-Based选择器：这种类型的选择器允许按标签键和值进行过滤。因此，在外行术语中，
  基于Equity的选择器将仅查找与标签具有完全相同短语的pod。 示例：假设您的标签键表示app = nginx，那么，
  使用此选择器，您只能查找标签应用程序等于nginx的那些pod。

* Selector-Based选择器：此类型的选择器允许根据一组值过滤键。因此，换句话说，
  基于Selector的选择器将查找已在集合中提及其标签的pod。 示例：假设您的标签键在（nginx，NPS，Apache）中显示应用程序。
  然后，使用此选择器，如果您的应用程序等于任何nginx，NPS或Apache，则选择器将其视为真实结果。
  
## 什么是Headless Service？
Headless Service类似于“普通”服务，但没有群集IP。此服务使您可以直接访问pod，而无需通过代理访问它。

## 使用Kubernetes时可以采取哪些最佳安全措施？
以下是使用Kubernetes时可以遵循的最佳安全措施：

![](https://img-blog.csdnimg.cn/img_convert/810b86c0e84859004e0f6b37326370e4.png)

## 什么是集群联邦？
在联邦集群的帮助下，可以将多个Kubernetes集群作为单个集群进行管理。因此，
您可以在数据中心/云中创建多个Kubernetes集群，并使用联邦来在一个位置控制/管理它们。

联合集群可以通过执行以下两项操作来实现此目的。请参考下图。

![](https://img-blog.csdnimg.cn/img_convert/3399970954c7dd17b0bb579ab2fd1068.png)


