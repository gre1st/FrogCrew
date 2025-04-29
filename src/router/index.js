import { createRouter, createWebHistory } from 'vue-router'

/* ─── views ─── */
import LoginView             from '@/views/LoginView.vue'
import CrewDashboardView      from '@/views/CrewDashboardView.vue'
import AdminDashboardView     from '@/views/AdminDashboardView.vue'
import ProfileView            from '@/views/ProfileView.vue'
import AvailabilityView       from '@/views/AvailabilityView.vue'
import ScheduleView           from '@/views/ScheduleView.vue'
import CrewListView           from '@/views/CrewListView.vue'
import AdminManageCrewView    from '@/views/AdminManageCrewView.vue'
import AdminReportsView       from '@/views/AdminReportsView.vue'
import InviteView             from '@/views/InviteView.vue'
import TradeBoardView         from '@/views/TradeBoardView.vue'
import RegisterView           from '@/views/RegisterView.vue'

/* ─── route table ─── */
const routes = [
  { path: '/',                name: 'Login',            component: LoginView },
  { path: '/dashboard',       name: 'Dashboard',        component: CrewDashboardView },
  { path: '/admin-dashboard', name: 'AdminDashboard',   component: AdminDashboardView },

  { path: '/profile',         name: 'Profile',          component: ProfileView },
  { path: '/availability',    name: 'Availability',     component: AvailabilityView },
  { path: '/schedule',        name: 'Schedule',         component: ScheduleView },
  { path: '/crew-list',       name: 'CrewList',         component: CrewListView },

  { path: '/admin/crew',      name: 'AdminCrew',        component: AdminManageCrewView },
  { path: '/admin/reports',   name: 'AdminReports',     component: AdminReportsView },
  { path: '/invite',          name: 'Invite',           component: InviteView },
  { path: '/tradeboard',      name: 'TradeBoard',       component: TradeBoardView },

  { path: '/register',        name: 'Register',         component: RegisterView },
]

const router = createRouter({
  history : createWebHistory(),
  routes,
})

/* ───────────────────────
   Global navigation guard
   ─────────────────────── */
router.beforeEach((to, from, next) => {

  /* read token / role from storage */
  const token = localStorage.getItem('token')
  let   role  = localStorage.getItem('role') || ''

  /* normalise role => 'ADMIN' / 'CREW' */
  role = role.replace(/[^a-z]/gi, '').toUpperCase()

  /* 1. not logged in */
  if (!token) {
    if (to.path === '/' || to.path === '/register') {
      return next()
    }
    return next('/')                       // force login
  }

  /* 2. logged in but hitting '/' should jump to a dashboard */
  if (to.path === '/') {
    return role === 'ADMIN'
      ? next('/admin-dashboard')
      : next('/dashboard')
  }

  /* 3. protect admin pages */
  if (to.path.startsWith('/admin') || to.path === '/admin-dashboard') {
    if (role !== 'ADMIN') {
      return next('/dashboard')            // block crew users
    }
  }

  /* 4. crew user trying to hit admin route alias */
  if (to.path === '/dashboard' && role === 'ADMIN') {
    return next('/admin-dashboard')
  }

  next()
})

export default router
