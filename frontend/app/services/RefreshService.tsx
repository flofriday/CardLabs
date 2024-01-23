import { getCookie, setCookie, deleteCookie } from "cookies-next";

/**
 * Returns if the refresh token is still valid or a new one has successfully been received
 */
export async function refreshAccessToken(): Promise<boolean> {
  const expireStr = localStorage.getItem("auth_token_expire");
  if (getCookie("auth_token") !== undefined) {
    if (expireStr !== null && Date.now() < Date.parse(expireStr)) {
      return true;
    }
  }

  const refreshToken = localStorage.getItem("refresh_token");
  if (refreshToken === undefined) {
    return false;
  }
  deleteCookie("auth_token");
  const response = await fetch("/api/authentication/refresh", {
    mode: "cors",
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      refreshToken,
    }),
  });
  if (response.status === 200) {
    const json = await response.json();
    setCookie("auth_token", json.token.token);
    localStorage.setItem("auth_token_expire", json.token.expiration);
    return true;
  }

  if (response.status === 401 || response.status === 403) {
    deleteCookie("auth_token");
    localStorage.removeItem("refresh_token");
    localStorage.removeItem("auth_token_expire");
  }

  return false;
}
