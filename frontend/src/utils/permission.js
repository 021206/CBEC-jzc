export function hasPerm(perm) {
    const perms = JSON.parse(localStorage.getItem('perms') || 'null');
    // null 表示超级管理员，拥有全部权限
    if (perms === null) return true;
    if (!Array.isArray(perms)) return false;
    return perms.includes(perm);
}