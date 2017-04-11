## Rapid Android Development

RAD包含了现今热门的技术之外（OKHttp、Rx、Aop、MVP），不赘述，新增了一些特性


> [English view README_EU.md](https://github.com/vihuela/RAD/blob/master/README_EU.md "英文查看")

![arch](http://i.imgur.com/9zyUglL.png)


----------
	
- ViewModel横竖屏复用
	- AndroidManifest下配置configChanges仅适合横竖屏布局一致的场景
	- 通常屏幕旋转View重新填充，但是数据却可以重用
	- 一些场景可以配合ViewModel中间类完成复用，参考[ImageFragment](https://github.com/vihuela/RAD/blob/master/app/src/main/java/worldgo/rad/ui/ImageFragment.java "show")

- 网络使用[OKNet](https://github.com/vihuela/OKNet "OKNet")
	- 完善的缓存、Rx、异常分发支持
- 完善的UI与网络、基类框架联动写法
- 基类自由组合，支持DataBinding
	- DataBing的布局xml文件需要包裹layout节点，include节点的也需要,布局中新增的component或id，可能需要在As的右侧面板Refresh All，或者Rebuild
	- 请查看项目基类继承链，如要使用butterknife，直接继承ViewModelBaseActivity即可
	- 业务基类仅将DataBinding作为替代butterknife用处，默认的layout绑定的变量id为BR.viewModel

- aop启用，在app module下[顶部apply plugin](https://github.com/vihuela/RAD/blob/master/app/build.gradle "apply plugin")放开注释

- 欢迎建议、留言
- [点击查看运行效果](http://v.youku.com/v_show/id_XMjY5OTY5MjM2OA==?spm=a2h3j.8428770.3416059.1)

### License
 
      
http://www.apache.org/licenses/LICENSE-2.0 





