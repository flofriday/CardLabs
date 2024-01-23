import Link from "next/link";

export default function SignInUp(): JSX.Element {
  return (
    <div className="flex space-x-6">
      <Link href="/login">
        <button
          className="btn bg-secondary hover:bg-accent text-text py-2 px-6 rounded-full shadow-md text-lg"
          id="login_button_navbar"
        >
          Login
        </button>
      </Link>
    </div>
  );
}
