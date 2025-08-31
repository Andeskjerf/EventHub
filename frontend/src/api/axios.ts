import axios from "axios";
import { logout } from "@/services/authentication";
import { clearAuth } from "@/services/storage";
import { userModule } from "@/stores/auth/module";

export const apiClient = axios.create({
	baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8090/api",
	withCredentials: true,
	timeout: 10000,
	headers: {
		"Content-Type": "application/json",
	},
});
let refreshAttempt = false;

apiClient.interceptors.response.use(
	(response) => {
		if (refreshAttempt) refreshAttempt = false;
		return response;
	},
	async (error) => {
		const originalRequest = error.config;

		if (error.response?.status === 401 && !originalRequest._retry) {
			originalRequest._retry = true;

			if (!refreshAttempt) {
				refreshAttempt = true;
				try {
					await apiClient.post("/auth/refresh");
					return apiClient(originalRequest);
				} catch (refreshError) {
					refreshAttempt = false;
					if (userModule.state.isAuthenticated) {
						await logout();
						clearAuth();
						userModule.actions.updateAuthState();
					}
					return Promise.reject(refreshError);
				}
			} else if (refreshAttempt && userModule.state.isAuthenticated) {
				await logout();
				clearAuth();
				userModule.actions.updateAuthState();
			}
		}

		return Promise.reject(error);
	},
);
