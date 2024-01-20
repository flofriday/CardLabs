import LeftPageHeader from "../components/leftPageHeader";
export default function Login(): JSX.Element {
  return (
    <div className="w-full h-full">
      <LeftPageHeader title="Login" />
      <div className="h-full flex items-center justify-center">
        <div className="bg-secondary rounded-xl">
          <a href="http://localhost:8080/oauth2/authorization/github">GitHub</a>
        </div>
      </div>
    </div>
  );
}
