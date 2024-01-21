// stackoverflow.com/questions/49819183/react-what-is-the-best-way-to-handle-login-and-authentication
"use client";
import Cookies from "js-cookie";

export async function isAuthenticated(): Promise<boolean> {
  const authToken = Cookies.get("auth_token");
  if (authToken === undefined) {
    return false;
  }
  return await validToken(authToken);
}

export async function validToken(jwt: string): Promise<boolean> {
  // this needs to change if the backend is located on a different url
  // sadly this can't be "api/authentication", but I dont know why
  const managementUrl = process.env.MANAGEMENT_HOST ?? "http://localhost:8080";

  const response = await fetch(`${managementUrl}/authentication`, {
    mode: "cors",
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: "Bearer " + jwt,
    },
  });

  return response.status === 200;
}
