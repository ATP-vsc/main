<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>员工注册</title>
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
            min-width: 320px;
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
            background: #28a745;
            color: #fff;
            padding: 8px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            margin-top: 14px;
        }
        input[type="submit"]:hover {
            background: #218838;
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
    <h2>员工注册</h2>
    <form action="register" method="post">
        姓名: <input type="text" name="username" required/><br/>  <%-- 用于存username字段 --%>
        密码: <input type="password" name="password" required/><br/>
        手机号: <input type="text" name="phone" required/><br/>
        <input type="submit" value="注册"/>
        <br/><br/>
        <a href="login.jsp">已有账号？去登录</a>
    </form>

</div>
<%-- 背景图片链接说明：将你想要的图片链接放在body的background url处 --%>
</body>
</html>