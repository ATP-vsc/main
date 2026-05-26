import { createApp } from 'vue' // 从vue导入createApp方法

import App from './App.vue' // 导入根组件A
import 'amfe-flexible' // 引入amfe-flexible用于移动端适配
import {
    ConfigProvider,
    Button,
    Icon,
    Search,
    Tab,
    Tabs,
    Checkbox,
    CheckboxGroup,
    ActionBarButton,
    ActionBarIcon,
    ActionBar,
    Field,
    Toast,
    Form,
    Swipe,
    SwipeItem,

} from 'vant'; // 从vant组件库导入Button组件
import store from './store';
import router from './router/index' // 导入路由配置
import '../src/common/css/base.less' // 引入less基础样式
const app = createApp(App); // 创建Vue应用实例
app.use(Button) // 注册Button组件
    .use(router) // 注册路由
    .use(ConfigProvider) // 注册ConfigProvider组件
    .use(Icon) // 注册Icon组件
    .use(Search) // 注册Search组件
    .use(ActionBar) // 注册ActionBar组件
    .use(ActionBarButton) // 注册ActionBarButton组件
    .use(ActionBarIcon) // 注册ActionBarIcon组件
    .use(Toast) // 注册Toast组件
    .use(Field) // 注册Field组件
    .use(Form) // 注册Form组件
    .use(Swipe) // 注册Swipe组件
    .use(SwipeItem) // 注册SwipeItem组件
    .use(Tab) // 注册Tab组件

.use(Tabs) // 注册Tabs组件S
    .use(Checkbox) // 注册Checkbox组件
    .use(CheckboxGroup) // 注册CheckboxGroup组件
    .use(store) // 注册Vuex状态管理
    .mount('#app') // 挂载应用到id为app的DOM元素上