import LeftPageHeader from "../components/NavBar/leftPageHeader";
import LoginForm from "./loginForm";
export default function Login(): JSX.Element {
  return (
    <div className="w-full h-full">
      <LeftPageHeader title="Login" />
      <div className="h-full flex items-center justify-center">
        <div className="bg-secondary rounded-xl">
          <LoginForm />
        </div>
      </div>
    </div>
  );
}
