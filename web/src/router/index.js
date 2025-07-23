import { createWebHistory, createRouter } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RecordIndexView from '../views/record/RecordView.vue'
import RankListView from '../views/ranklist/RankListView.vue'
import BotView from '../views/user/bots/UserBotsView.vue'
import ErrorView from '../views/error/NotFoundView.vue'
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView.vue'
import UserBotsView from '../views/user/bots/UserBotsView.vue'

import store from '../store/index'
// import { from } from 'core-js/core/array'

const routes = [
  { path: '/pk_index', name: 'pk_index', component: PkIndexView , meta:{ requestAuth : true}},
  { path: '/record/', component: RecordIndexView, meta:{ requestAuth : true} },
  { path: '/ranklist_index', name: 'ranklist_index', component: RankListView, meta:{ requestAuth : true} },
  { path: '/bot/', name : 'user_account_bot',component: BotView, meta:{ requestAuth : true} },
  { path: '/404/', name : 'error_index' , component: ErrorView , meta:{ requestAuth : true} },
  { path: '/user/login/',name:'user_account_login', component: UserAccountLoginView , meta:{ requestAuth : false}},
  { path: '/user/register/',name:'user_account_register', component: UserAccountRegisterView , meta:{ requestAuth : false}},
  { path: '/user/bot/' , name : 'user_account_bot' , component: UserBotsView , meta:{ requestAuth : true}},
  { path: '/:catchAll(.*)', redirect: '/404/'}, 
  
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from , next) =>{
  if(to.meta.requestAuth && !store.state.user.is_login){
    next({name : 'user_account_login'})
  }else{
    next()
  }
})

export default router