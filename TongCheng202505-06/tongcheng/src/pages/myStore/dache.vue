<template>
    <Header title="打车"></Header>
    <div class="main">
        <div class="header">
            <img src="../../assets/dache1001.jpg" alt="">
        </div>
        <div class="map-container">
            <!-- 固定边框 -->
            <div class="map-border">
                <!-- 可滑动的内部区域 -->
                <div class="map-content" ref="mapContent" @mousedown="startDrag" >
                    <img src="../../assets/dacheMap.png" alt="Map" class="map-image">
                </div>
            </div>
        </div>
        <div class="foot">
            <img src="../../assets/dache1002.jpg" alt="">
        </div>
    </div>

</template>

<script>
import Header from "@/components/Header.vue";
export default {
      components: {
                Header,
            },
    data() {
        return {
          
            isDragging: false,
            // 坐标
            startX: 0,
            startY: 0,

        };
    },
    methods: {
        //开始
        startDrag(e) {
            // 统一处理鼠标和触摸事件
            // 初始坐标
            const clientX = e.clientX;
            const clientY = e.clientY;
            //状态
            this.isDragging = true;

            this.startX = clientX - this.$refs.mapContent.offsetLeft;
            this.startY = clientY - this.$refs.mapContent.offsetTop;


            // 添加事件监听
            document.addEventListener('mousemove', this.onDrag);    
            document.addEventListener('mouseup', this.endDrag);
           
        },
        //进行中
        onDrag(e) {
            if (!this.isDragging) return;
            //阻止默认行为
            e.preventDefault();
            //获取当前指针坐标
            const clientX = e.clientX || e.clientX;
            const clientY = e.clientY || e.clientY;
            // 计算偏移量
            const x = clientX - this.startX;
            const y = clientY - this.startY;


        },
        //结束
        endDrag() {
            this.isDragging = false;

            // 移除事件监听
            document.removeEventListener('mousemove', this.onDrag);
            document.removeEventListener('mouseup', this.endDrag);

        }
    }
};
</script>

<style scoped>
.main{
   
    background: linear-gradient(to bottom, #e3f3f3, #f3f4f6);

}
.map-container {
    width: 100%;
    max-width: 800px;
    /* 容器最大宽度 */
    margin: 0 auto;
    padding: 0 20px;
    
}

.map-border {
    position: relative;
    width: 95%;
    height: 500px;
    border-radius: 12px;
    overflow: hidden;
}

.map-content {
    width: 100%;
    height: 100%;
    overflow: auto;
    /* 启用滚动 */
    cursor: grab;
    /* 抓取光标 */
    scrollbar-width: none;
    /* Firefox 隐藏滚动条 */
}

.map-content::-webkit-scrollbar {
    display: none;
    /* Chrome/Safari 隐藏滚动条 */
}

.map-image {
    display: block;
    width: 2000px;
    height: 2000px;
    object-fit: cover;
}

/* 拖动时改变光标 */
.map-content:active {
    cursor: grabbing;
}
</style>