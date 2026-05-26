// index.js
// node server/index.js
require('dotenv').config();
const express = require('express');
const mysql = require('mysql2/promise');
const cors = require('cors');

const app = express();
const PORT = process.env.PORT || 5000; // 改为 5000 或其他未被占用的端口
const corsOptions = {
    origin: ['http://localhost:8080', 'http://localhost:8081'], // 允许多个前端地址
    methods: ['GET', 'POST', 'PUT', 'DELETE'], // 允许的 HTTP 方法
    allowedHeaders: ['Content-Type', 'Authorization'], // 允许的请求头
};

app.use(cors(corsOptions));

app.use(express.json()); // 解析 application/json
// 创建连接池
const pool = mysql.createPool({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
    waitForConnections: true,
    connectionLimit: 10
});

// 测试数据库连接
app.get('/api/test-db', async(req, res) => {
    try {
        const [rows] = await pool.query('SELECT 1 + 1 AS solution');
        res.json({ success: true, result: rows[0].solution });
    } catch (err) {
        res.status(500).json({ error: '数据库连接失败' });
    }
});
// 确保已添加根路由（你代码中已有）
app.get('/', (req, res) => {
    res.send('后端服务运行中！请访问 /api/data 获取数据');
});
// 实际数据接口 (示例)
app.get('/api/products', async(req, res) => {
    try {
        const [rows] = await pool.query('SELECT * FROM products');
        res.json(rows);
    } catch (err) {
        res.status(500).json({ error: '查询失败' });
    }
});
// 用户注册接口
app.post('/api/register', async(req, res) => {
    try {
        const { username, password } = req.body;
        // 检查用户名是否已存在
        const [existingUsers] = await pool.query(
            'SELECT * FROM users WHERE username = ?', [username]
        );

        if (existingUsers.length > 0) {
            return res.status(400).json({ error: '用户名已存在' });
        }

        // 插入新用户
        await pool.query(
            'INSERT INTO users (username, password) VALUES (?, ?)', [username, password]
        );

        res.json({ success: true, message: '注册成功' });
    } catch (err) {
        res.status(500).json({ error: '数据库错误' });
    }
});

// 用户登录接口
app.post('/api/login', async(req, res) => {
    try {
        const { username, password } = req.body;
        // 查询用户
        const [users] = await pool.query(
            'SELECT * FROM users WHERE username = ? AND password = ?', [username, password]
        );

        if (users.length === 0) {
            return res.status(401).json({ error: '用户名或密码错误' });
        }

        res.json({ success: true, user: users[0] });
    } catch (err) {
        res.status(500).json({ error: '数据库错误' });
    }
});

app.listen(PORT, () => {
    console.log(`后端运行在 http://localhost:${PORT}`);
});