"use client";

// import { useRouter } from "next/router";
import LeftPageHeader from "../../components/leftPageHeader";
import { refreshAccessToken } from "@/app/services/RefreshService";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";

async function loginSuccessHandler(token: string): Promise<void> {
  localStorage.setItem("refresh_token", token);
  await refreshAccessToken();
}

export default function LoginSuccess(): JSX.Element {
  const [isError, setIsError] = useState(false);

  const searchParams = useSearchParams();
  const refreshToken = searchParams.get("refresh_token");

  useEffect(() => {
    if (refreshToken === null) {
      setIsError(true);
    } else {
      loginSuccessHandler(refreshToken).catch(() => {});
    }
  }, [refreshToken]);

  return (
    <div className="w-full h-full">
      <LeftPageHeader title="Login Success" />
      <div className="h-full flex items-center justify-center">
        <div className="">
          {isError ? "Login failed, try again..." : "Loading..."}
          <button
            className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg"
            onClick={() => {
              if (refreshToken === null) {
                setIsError(true);
              } else {
                loginSuccessHandler(refreshToken).catch(() => {});
              }
            }}
          >
            Set Token
          </button>
        </div>
      </div>
    </div>
  );
}
