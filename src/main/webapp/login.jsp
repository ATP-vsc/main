<%-- 美化登录页面：和首页一致风格 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            height: 100vh;
            width: 100vw;
            /* 背景图片设置，替换url为你的图片链接 */
            background: url('resources/beijing.png') no-repeat center center fixed; /* TODO: 在此处替换为实际图片地址 */
            background-size: cover;
        }
        .container {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: rgba(255, 255, 255, 0.9);
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0,0,0,0.2);
            padding: 40px 30px;
            min-width: 300px;
            text-align: center;
        }
        input[type="text"], input[type="password"] {
            width: 90%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 8px;
        }
        input[type="submit"] {
            background: #007bff;
            color: #fff;
            padding: 8px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            margin-top: 14px;
        }
        input[type="submit"]:hover {
            background: #0056b3;
        }
        a {
            color: #007bff;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        font[color="red"] {
            margin-top: 12px;
            display: block;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>登录</h2>
    <% if (request.getAttribute("errorMsg") != null) { %>
        <font color="red"><%= request.getAttribute("errorMsg") %></font>
    <% } %>
    <form action="login" method="post">
        姓名: <input type="text" name="username" required/><br/>
        密码: <input type="password" name="password" required/><br/>
        <input type="submit" value="登录"/>
        <br/><br/>
        <a href="register.jsp">没有账号？去注册</a>
    </form>

</div>
<%-- 背景图片链接说明：将你想要的图片链接放在body的background url处 --%>
</body>
</html>