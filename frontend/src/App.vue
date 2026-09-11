<template>
  <router-view />
</template>

<style>
/* 全局样式重置：让页面铺满整个浏览器 */
html, body, #app {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow: hidden; /* 防止出现双重滚动条 */
}

* {
  box-sizing: border-box;
}
</style>