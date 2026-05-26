import axios from 'axios';

// 创建带基础配置的实例
const apiClient = axios.create({
    baseURL: 'http://localhost:5000/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
});

// 用户注册
export const registerUser = (userData) => {
    return apiClient.post('/register', userData);
};

// 用户登录
export const loginUser = (credentials) => {
    return apiClient.post('/login', credentials);
};