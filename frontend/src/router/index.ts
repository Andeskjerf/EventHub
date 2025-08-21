import { createRouter, createWebHistory } from "vue-router";
import Authentication from "@/pages/Authentication.vue";
import Home from "@/pages/Home.vue";

const routes = [
	{ path: "/", component: Home },
	{ path: "/auth", component: Authentication },
];

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: routes,
});

export default router;
