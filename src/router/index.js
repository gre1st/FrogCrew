import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import CrewDashboardView from '../views/CrewDashboardView.vue'
import AdminDashboardView from '../views/AdminDashboardView.vue'
import ProfileView from '../views/ProfileView.vue'
import AvailabilityView from '../views/AvailabilityView.vue'
import ScheduleView from '../views/ScheduleView.vue'
import CrewListView from '../views/CrewListView.vue'
import AdminManageCrewView from '../views/AdminManageCrewView.vue'
import AdminReportsView from '../views/AdminReportsView.vue'
import InviteView from '../views/InviteView.vue'
import TradeBoardView from '../views/TradeBoardView.vue'
import RegisterView from '../views/RegisterView.vue'

const routes = [
  { path: '/', name: 'Login', component: LoginView },
  { path: '/dashboard', name: 'Dashboard', component: CrewDashboardView },
  { path: '/admin-dashboard', name: 'AdminDashboard', component: AdminDashboardView },
  { path: '/profile', name: 'Profile', component: ProfileView },
  { path: '/availability', name: 'Availability', component: AvailabilityView },
  { path: '/schedule', name: 'Schedule', component: ScheduleView },
  { path: '/crew-list', name: 'CrewList', component: CrewListView },
  { path: '/admin/crew', name: 'AdminCrew', component: AdminManageCrewView },
  { path: '/admin/reports', name: 'AdminReports', component: AdminReportsView },
  { path: '/invite', name: 'Invite', component: InviteView },
  { path: '/tradeboard', name: 'TradeBoard', component: TradeBoardView },
  { path: '/register', name: 'Register', component: RegisterView },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 🛡️ Navigation Guard for Admin pages
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  if (!token && to.path !== '/' && to.path !== '/register') {

    return next('/') // No token, force to login
  }

  if (to.path.startsWith('/admin') || to.path === '/admin-dashboard') {
    if (role !== 'admin') {
      return next('/dashboard') // Block crew members from admin pages
    }
  }

  next()
})

export default router
