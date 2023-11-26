import LeftPageHeader from "../components/leftPageHeader";
import RegisterForm from "./registerForm";
export default function Register(): JSX.Element {
  return (
    <div className="w-full h-full">
      <LeftPageHeader title="Register" />
      <div className="h-full flex items-center justify-center">
        <div className="bg-secondary rounded-xl">
          <RegisterForm />
        </div>
      </div>
    </div>
  );
}
