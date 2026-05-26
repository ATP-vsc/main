// 修改main.js中的loadContent函数

// 处理升职申请的批准和拒绝操作
function handlePromotionRequest(requestId, action) {
    // 创建XMLHttpRequest对象
    var xhr = new XMLHttpRequest();
    
    // 构建请求URL
    var url = 'jobChange?action=' + action + '&requestId=' + requestId;
    
    // 配置请求
    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    
    // 处理响应
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                // 解析JSON响应
                var response = JSON.parse(xhr.responseText);
                
                // 根据响应结果显示消息
                if (response.success) {
                    alert('操作成功：' + response.message);
                    // 重定向到index页面
                    window.location.href = 'index.jsp';
                } else {
                    alert('操作失败：' + response.message);
                }
            } else {
                alert('网络错误，请重试');
            }
        }
    };
    
    // 发送请求
    xhr.send();
}

function loadContent(page) {
    var xhr = new XMLHttpRequest();

    // 处理不同类型的页面请求
    var requestUrl = page;
    if (page === 'team') {
        requestUrl = 'team';
    } else if (page === 'department') {
        requestUrl = 'department';
    } else if (page === 'userInfo') {
        requestUrl = 'userInfo';
    } else if (page === 'userAttendance') {
        requestUrl = 'userAttendance';
    } else if (page === 'project') {
        requestUrl = 'project';
    } else if (page === 'companyAttendance') {
        requestUrl = 'companyAttendance';
    } else if (page === 'companyFinancial') {
        requestUrl = 'companyFinancial';
    } else if (page === 'personnelManagement') {
        requestUrl = 'personnelManagement';
    } else if (page === 'messages') {
        requestUrl = 'messages?action=list';  // 添加action参数
    } else if (page === 'promotions') {
        requestUrl = 'promotions';
    } else if (page === 'jobChange') {
        requestUrl = 'jobChange';
    }

    xhr.open('GET', requestUrl, true);
    xhr.onreadystatechange = function(){
        if(xhr.readyState === 4){
            if(xhr.status === 200){
                // 确保内容被正确加载到容器中
                document.getElementById('main-content').innerHTML = xhr.responseText;

                // 添加一个延迟以确保DOM更新完成
                setTimeout(function() {
                    // 确保所有内容框都应用了居中样式
                    var contentBoxes = document.querySelectorAll('.content-box, .container');
                    contentBoxes.forEach(function(box) {
                        box.style.margin = '0 auto';
                    });
                }, 50);

            } else {
                console.error('加载失败:', xhr.status, xhr.statusText);
                document.getElementById('main-content').innerHTML =
                    '<div class="content-box" style="text-align:center;margin:50px auto;"><p style="color:#f00;">页面加载失败，请稍后重试~</p></div>';
            }
        }
    };
    xhr.send();
}