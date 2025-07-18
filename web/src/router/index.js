import { createWebHistory, createRouter } from 'vue-router'

import PkIndexView from '../views/pk/PkIndexView.vue'
import RecordIndexView from '../views/record/RecordView.vue'
import RankListView from '../views/ranklist/RankListView.vue'
import BotView from '../views/user/bots/UserBotsView.vue'
import ErrorView from '../views/error/NotFoundView.vue'

// import { from } from 'core-js/core/array'

const routes = [
  { path: '/pk_index', name: 'pk_index', component: PkIndexView },
  { path: '/record/', component: RecordIndexView },
  { path: '/ranklist_index', name: 'ranklist_index', component: RankListView },
  { path: '/bot/', component: BotView },
  { path: '/404/', component: ErrorView }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router