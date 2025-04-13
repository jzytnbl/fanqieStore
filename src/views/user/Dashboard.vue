<template>
  <div class="dashboard-container">
    <el-card class="dashboard-card">
      <h2>个人信息</h2>
      <el-form :model="userInfo" label-width="80px" ref="userFormRef">
        <el-form-item label="用户名">
          <el-input v-model="userInfo.username" disabled />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="userInfo.name" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userInfo.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userInfo.telephone" />
        </el-form-item>
        <el-form-item label="所在地">
          <el-input v-model="userInfo.location" />
        </el-form-item>
        <!-- 添加跳转到修改个人信息页面的按钮 -->
        <el-form-item>
          <el-button type="primary" @click="goToEditProfile">修改个人信息</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router'; // 引入 vue-router
import {fetchUserDetail, getUserInfo, updateUser} from '@/api/accounts';

const router = useRouter(); // 使用 router

const userInfo = ref({
  username: '',
  name: '',
  email: '',
  telephone: '',
  location: '',
});


const updateUserInfo = async () => {
  try {
    await updateUser(userInfo.value);
    ElMessage.success('信息更新成功');
  } catch (error) {
    ElMessage.error('更新失败');
  }
};

const goToEditProfile = () => {
  router.push('/edit-profile'); // 跳转到修改个人信息页面
};
// 获取用户详情
const loadUserData = async () => {
  try {
    // 从localStorage获取username
    const username = sessionStorage.getItem('username');
    if (!username) {
      ElMessage.error('请先登录');
      return router.push('/login');
    }
    const token = sessionStorage.getItem('token');
    console.log('token in dashboard:'+token);
    console.log('username in dashboard:'+username);
    // 获取用户详情数据
    const response = await fetchUserDetail(username,token);
    userInfo.value = response.data; // 假设返回的数据结构匹配
    console.log(userInfo.value);
  } catch (error) {
    ElMessage.error('获取用户信息失败');
  }
};

onMounted(() => {
  loadUserData();
});
</script>

<style scoped>
.dashboard-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}
.dashboard-card {
  width: 400px;
  padding: 20px;
  text-align: center;
}
</style>
