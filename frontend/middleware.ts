import { isAuthenticated } from "./app/services/AuthenticationService";
import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

//protected sites
const protectedRoutes = ["/settings"];


export default function middleware(req: NextRequest) {
  if (isAuthenticated() == false && protectedRoutes.includes(req.nextUrl.pathname)) {
    const absoluteURL = new URL("/unauthorized", req.url);
    return NextResponse.rewrite(absoluteURL.toString());
  }
}