## Rapid Android Development

RAD includes the current popular technology (OKHttp, Rx, Aop, MVP), do not repeat, add some features

![arch](http://i.imgur.com/9zyUglL.png)


----------
	
- ViewModel Vertical and horizontal screen reuse
	- AndroidManifest config *configChanges* Only suitable for horizontal and vertical screen layout consistent scene
	- The screen is usually rotated to re-fill, but the data can be reused
	- Some scenes can be combined with the ViewModel middle class to reuse, refer to [ImageFragment](https://github.com/vihuela/RAD/blob/master/app/src/main/java/worldgo/rad/ui/ImageFragment.java "show")


- Network use [OKNet](https://github.com/vihuela/OKNet "OKNet")
	- Perfect cache, Rx, exception distribution support
- Perfect UI and network, base class framework linkage writing
- Base class free combination, support DataBinding
	- DataBing layout xml file needs to wrap the **layout** node, the include node also needs, the layout of the new component or id, may need to be in the right panel of Android Studio **Refresh All**, or **Rebuild**
	- Please see the project base class inheritance chain, if you want to use butterknife, direct inheritance ViewModelBaseActivity can
	- Business base class only DataBinding as an alternative to butterknife use, the default layout bound variable id BR.viewModel

- aop enable，in app module [apply plugin](https://github.com/vihuela/RAD/blob/master/app/build.gradle "apply plugin")Release the comment

- Welcome advice, issue
- [Click to view the running results](http://v.youku.com/v_show/id_XMjY5OTY5MjM2OA==?spm=a2h3j.8428770.3416059.1)

### License
 
      
http://www.apache.org/licenses/LICENSE-2.0 