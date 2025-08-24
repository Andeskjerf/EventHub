import { createRouter, createWebHistory } from "vue-router";
import ActivityInfo from "@/pages/ActivityInfo.vue";
import Authentication from "@/pages/Authentication.vue";
import CreateActivity from "@/pages/CreateActivity.vue";
import Home from "@/pages/Home.vue";

const routes = [
	{ path: "/", component: Home },
	{ path: "/create", component: CreateActivity },
	{ path: "/activities/:id", component: ActivityInfo },
	{ path: "/auth", component: Authentication },
];

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: routes,
});

export default router;
