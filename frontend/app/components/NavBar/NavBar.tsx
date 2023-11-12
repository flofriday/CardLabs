import Link from "next/link";
import SignInUp from "./SignInUp";
import Profile from "./Profile";
import { isAuthenticated } from "../../services/AuthenticationService";

export default function NavBar() {
  return (
    <div className="w-screen h-20 bg-primary flow-root">
      <div className="h-full flex items-center space-x-6 w-fit float-left">
        <Link href="/" className="mx-3 tracking-[.3em] font-bold text-[2em] ">
          Card Labs
        </Link>
        <Link href="/" className="text-xl  hover:text-accent">
          Home
        </Link>
        <Link href="/" className="text-xl  hover:text-accent">
          Leaderboard
        </Link>
        <Link href="/" className="text-xl  hover:text-accent">
          Help
        </Link>
        <Link href="/" className="text-xl  hover:text-accent">
          Rules
        </Link>
      </div>
      <div className="w-fit flex h-full float-right items-center mr-5">
        {isAuthenticated() ? <Profile /> : <SignInUp />}
      </div>
    </div>
  );
}
