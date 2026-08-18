<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background: #304156">
      <div class="logo">CBEC 后台</div>
      <el-menu
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          :default-active="activeMenu"
          router
      >
        <template v-for="menu in menus" :key="menu.id">
          <el-sub-menu v-if="menu.children && menu.children.length > 0" :index="menu.path">
            <template #title>
              <span>{{ menu.menuName }}</span>
            </template>
            <el-menu-item v-for="child in menu.children" :key="child.id" :index="child.path">
              {{ child.menuName }}
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item v-else :index="menu.path">
            <span>{{ menu.menuName }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background: #fff; border-bottom: 1px solid #ddd; display: flex; justify-content: space-between; align-items: center;">
        <span>欢迎 {{ nickname }}</span>
        <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { getMenus } from '../api/auth';

const router = useRouter();
const menus = ref([]);
const nickname = ref('管理员');
const activeMenu = ref('/');

const loadMenus = async () => {
  try {
    const res = await getMenus();
    if (res.code === 200) {
      menus.value = res.data || [];
    }
  } catch (e) {
    console.error('加载菜单失败', e);
  }
};

const handleLogout = () => {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  ElMessage.success('已退出');
  router.push('/login');
};

onMounted(() => {
  loadMenus();
});
</script>

<style scoped>
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #1f2d3d;
}
</style>