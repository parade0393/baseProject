## 项目记录
1. `colorPrimary` 状态栏颜色
2. `colorAccent` 控件获得焦点时的颜色

## 腾讯tbs继承
1. libs添加依赖
2. 在src/main/jniLibs/armeabi/下添加so文件
3. 加入权限声明
4. 在BaseApplication中初始化

## 腾讯Buggly集成
1. 导入依赖，加入相应的权限
2. 初始化，填写注册时申请的id，即可
## 腾讯乐固加固
1. 上传Apk加固
2. 重新签名

## 蒲公英集成
1. build.gradle导入依赖，配置meta-data
2. 配置自动更新

## EnentBus封装
1. 封装EventBusUtil工具类
2. 封装Event事件基类
3. 封装事件Code事件常量类 EventCode，通过code区分事件类型
4. 在BaseActivity和BaseFragment封装注册和取消注册
5. 发送事件例子`EventBusUtil.sendEvent(new Event(Constant.EventCode.A,true));`
6.在需要接受事件的类中定义接受事件，根据code来判来处理事件

### 疑问
1. MultiDex
2. 项目crash日志收集