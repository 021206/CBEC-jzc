<template>
  <div class="login-container">
    <div class="login-box">
      <h2>跨境电商后台管理系统</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="账号">
          <el-input v-model="form.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="form.password" placeholder="请输入密码" @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%">登 录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { login } from '../api/auth';

const router = useRouter();
const form = reactive({ username: 'admin', password: '021206' });

const handleLogin = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入账号和密码');
    return;
  }
  try {
    const res = await login(form.username, form.password);
    if (res.code === 200) {
      console.log('登录成功，返回数据:', res.data);
      localStorage.setItem('accessToken', res.data.accessToken);
      localStorage.setItem('refreshToken', res.data.refreshToken);
      ElMessage.success('登录成功');
      router.push('/');
    } else {
      ElMessage.error(res.msg || '登录失败');
    }
  } catch (error) {
    console.error('登录异常', error);
  }
};
</script>

<style scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  background: #2b3a4a;
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.3);
}
.login-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
</style>