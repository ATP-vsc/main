//另外的旅游数据
//滚动框
export const lvyouData = {
    adimages: [
        require('@/assets/lyad1.jpg'),
        require('@/assets/lyad2.jpg'),
        require('@/assets/lyad3.jpg'),
    ],
    destination_list: [
        // 旅游景点
        {
            tab: "热门推荐",
            data: [{
                    pic: require('@/assets/home10.jpg'),
                    title: "四川九寨沟",
                    sales: "3天2晚",
                    price: "404",
                    label: ["特色熊猫餐", "黄龙都江堰"],
                    router: '/jiuzhaigo',
                },
                {
                    pic: require('@/assets/home11.jpg'),
                    title: "来'北疆'撒野",
                    sales: "7天6晚",
                    price: "200",
                    label: ["赛里木湖", "赠送旅拍"],
                    router: '/xingjiang',
                },
                {
                    pic: require('@/assets/home13.jpg'),
                    title: "一见钟秦西安",
                    sales: "3天2晚",
                    price: "1582",
                    label: ["舒适酒店", "赠送驼铃"],
                    router: '/xian',
                },
                {
                    pic: require('@/assets/home14.jpg'),
                    title: "圆梦北京城",
                    sales: "5天4晚",
                    price: "2316",
                    label: ["八达岭长城", "故宫博物院"],
                    router: '/beij1',
                },
                {
                    pic: require('@/assets/home15.jpg'),
                    title: "北京高端团",
                    sales: "4天3晚",
                    price: "564",
                    label: ["升旗仪式", "高档酒店"],
                    router: '/beijing2',
                },
                {
                    pic: require('@/assets/home17.jpg'),
                    title: "亚特兰蒂斯",
                    sales: "5天4晚",
                    price: "2751",
                    label: ["蜈支洲后海村", "游艇出海"],
                    router: '/shanya',
                },
            ],
        },
    ],

};