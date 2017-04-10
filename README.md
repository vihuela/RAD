## Rapid Android Development

###RAD包含了现今热门的技术之外（OKHttp、Rx、Aop、MVP），不赘述，新增了一些特性

![arch](http://i.imgur.com/9zyUglL.png)


----------
	
- ViewModel横竖屏复用
	- AndroidManifest下配置configChanges仅适合横竖屏布局一致
	- 通常屏幕旋转View重新填充，但是数据却可以重用
	- 一些场景可以配合ViewModel中间类完成复用，参考ImageFragment


- 网络使用[OKNet](https://github.com/vihuela/OKNet "OKNet")
	- 完善的缓存、Rx、异常分发支持
- 完善的UI与网络、基类框架联动写法
- 基类自由组合，项目仅包含使用DataBinding使用场景
	- DataBing的layout文件需要包裹layout节点，include节点的layout也一样,新增layout的component或id，可能需要在As的右侧面板Refresh All，或者Rebuild
	- 如要使用butterknife，直接继承ViewModelBaseActivity即可
	- 默认的layout绑定的变量id为BR.viewModel

- 欢迎建议、留言
- [点击查看运行效果](http://v.youku.com/v_show/id_XMjY5OTY5MjM2OA==?spm=a2h3j.8428770.3416059.1)

### License
 
      
http://www.apache.org/licenses/LICENSE-2.0 





