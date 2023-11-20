import { toast } from "react-toastify";
import { setCookie } from "cookies-next";

// this function returns the jwt on success and null on failure
export async function login(
  username: string,
  password: string
): Promise<boolean> {
  const response = await fetch("api/authentication/login", {
    mode: "cors",
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  });

  if (response.status === 401) {
    toast.error("Wrong username or password");
    return false;
  } else if (response.status !== 200) {
    toast.error("Other error: " + response.status);
    return false;
  }
  const json = await response.json();
  setCookie("auth_token", json.jwt);

  return true;
}

export async function register(
  username: string,
  email: string,
  password: string
): Promise<boolean> {
  const response = await fetch("api/account", {
    mode: "cors",
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, email, password }),
  });

  if (response.status === 201) {
    return true;
  } else {
    toast.error("Invalid response on register: " + response.status);
    return false;
  }
}
