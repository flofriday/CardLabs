"use client";

// import { useRouter } from "next/router";
import LeftPageHeader from "../../components/leftPageHeader";
import { refreshAccessToken } from "@/app/services/RefreshService";
import { useRouter, useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";

async function loginSuccessHandler(token: string): Promise<void> {
  localStorage.setItem("refresh_token", token);
  await refreshAccessToken();
}

export default function LoginSuccess(): JSX.Element {
  const [isError, setIsError] = useState(false);

  const searchParams = useSearchParams();
  const refreshToken = searchParams.get("refresh_token");
  const router = useRouter();

  useEffect(() => {
    if (refreshToken === null) {
      setIsError(true);
    } else {
      loginSuccessHandler(refreshToken)
        .then(() => {
          router.replace("/dashboard");
          router.refresh();
        })
        .catch(() => {});
    }
  }, [refreshToken, router]);

  return (
    <div className="w-full h-full">
      <LeftPageHeader title="Login Success" />
      <div className="h-full flex items-center justify-center">
        <div className="text-6xl">
          {isError ? "Login failed, try again..." : "Loading..."}
        </div>
      </div>
    </div>
  );
}
