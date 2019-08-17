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

## 蒲公英集成
1. build.gradle导入依赖，配置meta-data
2. 配置自动更新

### 疑问
1. MultiDex
2. 项目crash日志收集
3. buggly