import { createRouter, createWebHistory } from 'vue-router';

// 导入路由需要的自定义组件
import Mine from '../pages/mine/Mine.vue'
import MyTravel from '../pages/myTravel/MyTravel.vue'
import MyHome from '../pages/myHome/MyHome.vue'
import MyOrder from '../pages/myOrder/MyOrder.vue'
import huoche from '@/pages/myStore/huoche.vue';
import jiudian from '@/pages/myStore/jiudian.vue'
import dache from '@/pages/myStore/dache.vue'
import feiji from '@/pages/myStore/feiji.vue'
import lvyou from '@/pages/myStore/Lvyou/lvyou.vue'
import Login from '@/pages/login/Login.vue';
import Register from '@/pages/register/Register.vue';
import SearchResults from '@/pages/myStore/HomeDown/SearchResults.vue';
import bedselection from '@/pages/myHome/hotelitem/Bedselection.vue'
import feijiStore from '@/pages/myStore/feijiStore.vue';
import staff from '@/pages/myStore/staff.vue';
import coupon from '@/pages/myStore/coupon.vue';


//首页下拉框的router
import lvxingtuan from '@/pages/myStore/HomeDown/lvxingtuan.vue';
import guihupack from '@/pages/myStore/HomeDown/guihupack.vue';
import kaiyuanshi from '@/pages/myStore/HomeDown/kaiyuanshi.vue';
import sanshisan from '@/pages/myStore/HomeDown/sanshisan.vue';
import xuanmiao from '@/pages/myStore/HomeDown/xuanmiao.vue';
import shiniushan from '@/pages/myStore/HomeDown/shiniushan.vue';
import xunpucun from '@/pages/myStore/HomeDown/xunpucun.vue';
import jiuzhaigo from '@/pages/myStore/HomeDown/jiuzhaigo.vue';
import fushishan from '@/pages/myStore/HomeDown/fushishan.vue';
import xian from '@/pages/myStore/HomeDown/xian.vue';
import beij1 from '@/pages/myStore/HomeDown/beij1.vue';
import beijing2 from '@/pages/myStore/HomeDown/beijing2.vue';
import shanya from '@/pages/myStore/HomeDown/shanya.vue';
import xingjiang from '@/pages/myStore/HomeDown/xingjiang.vue';
import huangjinghaian from '@/pages/myStore/HomeDown/huangjinghaian.vue';
import zhangjie from '@/pages/myStore/HomeDown/zhangjie.vue';
import wangxingling from '@/pages/myStore/HomeDown/wangxingling.vue';
import wanglihong from '@/pages/myStore/HomeDown/wanglihong.vue';
import xishi from '@/pages/myStore/HomeDown/xishi.vue';
import wubai from '@/pages/myStore/HomeDown/wubai.vue';
import caomei from '@/pages/myStore/HomeDown/caomei.vue';


// 定义路由
const routes = [
    { path: '/', redirect: '/home' },
    { path: '/home', component: MyHome },
    { path: '/travel', component: MyTravel },
    { path: '/register', component: Register },
    {
        path: '/bedselection',
        name: 'Bedselection',
        component: bedselection
    },
    {
        path: '/me',
        component: Mine,
        meta: {
            isAuth: true, // 需要登录才能访问
        }
    },
    {
        path: '/order',
        component: MyOrder,
        meta: {
            isAuth: true, // 需要登录才能访问
        }
    },
    { path: '/login', component: Login, },
    { path: '/jiudian', component: jiudian },
    { path: '/dache', component: dache },
    { path: '/feiji', component: feiji },
    { path: '/lvyou', component: lvyou },
    { path: '/huoche', component: huoche },
    { path: '/search-results', component: SearchResults, name: 'SearchResults' },
    { path: '/staff', component: staff },
    { path: '/coupon', component: coupon },

    // 首页下拉框的路由
    { path: '/lvxingtuan', component: lvxingtuan },
    { path: '/guihupack', component: guihupack },
    { path: '/kaiyuanshi', component: kaiyuanshi },
    { path: '/sanshisan', component: sanshisan },
    { path: '/xuanmiao', component: xuanmiao },
    { path: '/shiniushan', component: shiniushan },
    { path: '/xunpucun', component: xunpucun },
    { path: '/jiuzhaigo', component: jiuzhaigo },
    { path: '/fushishan', component: fushishan },
    { path: '/xian', component: xian },
    { path: '/beij1', component: beij1 },
    { path: '/beijing2', component: beijing2 },
    { path: '/shanya', component: shanya },
    { path: '/xingjiang', component: xingjiang },
    { path: '/huangjinghaian', component: huangjinghaian },
    { path: '/zhangjie', component: zhangjie },
    { path: '/wangxingling', component: wangxingling },
    { path: '/wanglihong', component: wanglihong },
    { path: '/xishi', component: xishi },
    { path: '/wubai', component: wubai },
    { path: '/caomei', component: caomei },
    { path: '/Bedselection', component: bedselection, name: 'bedselection' },
    { path: '/feijiStore', component: feijiStore, name: 'feijiStore' }

];

// 创建路由对象el
const router = createRouter({
        history: createWebHistory(),
        routes // （缩写）相当于 routes: routes
    })
    // 全局前置守卫
router.beforeEach((to, from, next) => {
    if (to.meta.isAuth) {
        if (localStorage.isLogin === '1') {
            next();
        } else {

            next('/login');
        }
    } else {
        next();
    }
})

export default router;