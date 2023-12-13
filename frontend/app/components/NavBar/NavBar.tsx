import Link from "next/link";
import SignInUp from "./SignInUp";
import Profile from "./Profile";
import { isAuthenticated } from "../../services/AuthenticationService";

export default async function NavBar(): Promise<JSX.Element> {
  return (
    <div className="w-full h-14 bg-primary flow-root fixed z-30">
      <div className="h-full flex items-center space-x-6 w-fit float-left">
        {(await isAuthenticated()) ? (
          <Link
            id="home_authenticated"
            href="/dashboard"
            className="mx-3 tracking-[.3em] font-bold text-[1.6em]"
          >
            Card Labs
          </Link>
        ) : (
          <Link
            id="home_unauthenticated"
            href="/"
            className="mx-3 tracking-[.3em] font-bold text-[1.6em]"
          >
            Card Labs
          </Link>
        )}

        <Link href="/" className="text-base  hover:text-accent max-sm:hidden">
          Leaderboard
        </Link>
        <Link
          href="/help"
          className="text-base  hover:text-accent max-sm:hidden"
        >
          Help
        </Link>
        <Link
          href="/rules"
          className="text-base  hover:text-accent max-sm:hidden"
        >
          Rules
        </Link>
      </div>
      <div className="w-fit flex h-full float-right items-center mr-5">
        {(await isAuthenticated()) ? <Profile /> : <SignInUp />}
      </div>
    </div>
  );
}