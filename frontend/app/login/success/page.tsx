"use client";

//import { useRouter } from "next/router";
import LeftPageHeader from "../../components/leftPageHeader";
import { refreshAccessToken } from "@/app/services/RefreshService";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";

async function loginSuccessHandler(token: string): Promise<void> {
  localStorage.setItem("refresh_token", token);
  console.log(token);
  await refreshAccessToken();
  console.log("Flotschi");
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
        </div>
      </div>
    </div>
  );
}
