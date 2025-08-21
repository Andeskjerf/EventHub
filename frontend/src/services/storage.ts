export const STORAGE_KEYS = {
	TOKEN: "jwt-token",
	USERNAME: "authenticated-username",
};

export const getToken = () => localStorage.getItem(STORAGE_KEYS.TOKEN);
export const getUsername = () => localStorage.getItem(STORAGE_KEYS.USERNAME);
export const setToken = (token: string) =>
	localStorage.setItem(STORAGE_KEYS.TOKEN, token);
export const setUsername = (username: string) =>
	localStorage.setItem(STORAGE_KEYS.USERNAME, username);
export const clearAuth = () => {
	localStorage.removeItem(STORAGE_KEYS.TOKEN);
	localStorage.removeItem(STORAGE_KEYS.USERNAME);
};
export const isAuthenticated = () => !!getToken();
