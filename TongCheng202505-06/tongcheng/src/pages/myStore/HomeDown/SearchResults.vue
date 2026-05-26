<template>
    <Header title="查询结果"></Header>
    <div class="main">
        <div class="search-results">
            <!-- 非空 -->
            <div v-if="results.length > 0" class="results-container">
                <div v-for="(item, index) in results" :key="index" class="result-item">
                    <!-- 加router -->
                    <router-link :to="item.router" class="item-link">
                        <div class="item-content">
                            <div class="image-container">
                                <img :src="item.pic" alt="" class="result-image">
                            </div>
                            <!-- info -->
                            <div class="result-info">
                                <div class="title">{{ item.title }}</div>
                                <div class="sales">距离: {{ item.sales }}</div>
                                <div class="price">浏览量 {{ item.price }}</div>
                                <!-- 标签for -->
                                <div class="labels">
                                    <van-tag v-for="(tag, tagIndex) in item.label" :key="tagIndex" type="primary"
                                        class="tag">
                                        {{ tag }}
                                    </van-tag>
                                </div>
                            </div>

                        </div>
                    </router-link>

                </div>
            </div>
            <!-- 空 -->
            <div v-else class="no-results">
                <van-empty image="search" description="没有找到相关结果" class="custom-empty">
                    <!-- button -->
                    <van-button round type="primary" class="bottom-button" @click="$router.push('/')">
                        返回首页
                    </van-button>
                </van-empty>
            </div>
        </div>
    </div>

</template>

<script>
import Header from '@/components/Header.vue';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { homeData } from '@/data/homeData';
import { Empty, Button, Tag } from 'vant';

export default {
    components: {
        Header,
        [Empty.name]: Empty,  // 注册 Empty 组件
        [Button.name]: Button,
        [Tag.name]: Tag,
    },
    setup() {
        const route = useRoute();
        const results = ref([]);
        const searchTerm = ref('');


        onMounted(() => {
            searchTerm.value = route.query.q || '';// 获取路由传递的搜索关键词
            performSearch();// 执行搜索
        });// 页面加载时获取搜索关键词并执行搜索

        const performSearch = () => {
            if (!searchTerm.value.trim()) {
                results.value = [];
                return;
            }

            const allItems = [];
            const searchLower = searchTerm.value.toLowerCase();// 转换搜索关键词为小写

            // 添加景点/演出数据
            homeData.centent_nav_list.forEach(category => {
                category.data.forEach(item => {
                    allItems.push({ ...item, type: 'attraction' });// 添加类型字段
                });
            });

            // 添加数据
            homeData.hotels.forEach(hotel => {
                allItems.push({
                    pic: hotel.img,//转换
                    title: hotel.title,
                    sales: hotel.distance || "未知距离",
                    price: hotel.currentPrice.toString(),
                    label: hotel.label || hotel.facilities || [],
                    desc: hotel.desc, // 添加描述字段
                    router: `/hotel-detail/${hotel.id}`,
                    type: 'hotel'
                });
            });

            // 扩展搜索条件
            results.value = allItems.filter(item => {
                return (
                    item.title.toLowerCase().includes(searchLower) ||
                    (item.label && item.label.some(tag =>
                        tag.toLowerCase().includes(searchLower))) ||
                    (item.desc && item.desc.toLowerCase().includes(searchLower))
                );// 检查标题、标签和描述是否包含搜索关键词，三元
            });
        };
        return {
            results,
            searchTerm,
            Header
        };
    }
};
</script>

<style lang="less" scoped>
.main {

    background-color: #f8f8f8;
    min-height: calc(100vh - 70px); // 减去头部高度
    box-sizing: border-box; // 确保padding和border不影响总高度
}

.search-results {
    padding: 10px;
    background-color: #f8f8f8;
    min-height: 100vh; //最小高度100%
    box-sizing: border-box;
}

.results-container {
    padding-top: 10px;
}

.result-item {
    background-color: white;
    border-radius: 15px;
    margin-bottom: 15px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: transform 0.3s ease, box-shadow 0.3s ease;// 添加平滑过渡效果
}

.result-item:hover {
    transform: translateY(-3px);// 鼠标悬停时轻微上移
    box-shadow: 0 6px 15px rgba(0, 0, 0, 0.12);// 鼠标悬停时增加阴影效果
}

.item-link {
    display: block;
    text-decoration: none;// 去除链接下划线
    color: inherit;
}

.item-content {
    display: flex;
    padding: 15px;
    gap: 15px;
}

.image-container {
    flex-shrink: 0;
    border-radius: 10px;
    overflow: hidden;
    width: 120px;
    height: 120px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.result-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
}

.result-image:hover {
    transform: scale(1.05);
}

.result-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding-right: 5px;
}

.title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 5px;
    color: #333;
    line-height: 1.3;
}

.sales {
    font-size: 14px;
    color: #666;
    margin-bottom: 5px;
}

.price {
    font-size: 18px;
    font-weight: bold;
    color: #ff6b00;
    margin-bottom: 10px;
}

.labels {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
    margin-top: auto;
}

.tag {
    background-color: #e6f7ff;
    color: #1890ff;
    border: 1px solid #91d5ff;
    border-radius: 4px;
    padding: 2px 8px;
    font-size: 12px;
    line-height: 1.5;
}

.no-results {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 400px;
    background-color: white;
    border-radius: 16px;
    padding: 30px;
    margin-top: 30px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
}

.custom-empty {
    text-align: center;
    padding: 30px 0;

    .van-empty__image {
        width: 180px;
        height: 180px;
    }

    .van-empty__description {
        font-size: 18px;
        color: #555;
        margin-top: 15px;
    }

    .bottom-button {
        margin-top: 5px;
        width: 180px;
        height: 44px;
        font-size: 20px;
        background: linear-gradient(135deg, #8cdeb9, #6bc4a6);
        border: none;
        box-shadow: 0 4px 10px rgba(140, 222, 185, 0.4);
    }
}
</style>