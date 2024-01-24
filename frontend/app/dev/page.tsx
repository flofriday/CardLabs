"use client";
import { setCookie } from "cookies-next";
import * as jose from "jose";
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime";
import { useRouter } from "next/navigation";

async function submitHandler(
  e: React.SyntheticEvent,
  router: AppRouterInstance
): Promise<void> {
  e.preventDefault();
  const target = e.target as HTMLFormElement;
  const id = (target.querySelector('input[name="id"]') as HTMLInputElement)
    .value;
  const email = (
    target.querySelector('input[name="email"]') as HTMLInputElement
  ).value;

  await setToken(parseInt(id, 10), email, router);
}

async function setToken(
  id: number,
  email: string,
  router: AppRouterInstance
): Promise<void> {
  const secret =
    "2e0377d9c56d8a51ed8cfc10a68bda5b3cdc7453a6c7b6ac4728d93e052618051bba4534b146e3cff10bed31224793cb46f78a628b8d6dc08b1b5496a05cf488";
  const alg = "HS512";

  const authToken = await new jose.SignJWT({
    "account-id": id,
    "account-email": email,
    "token-type": "ACCESS_TOKEN",
  })
    .setProtectedHeader({ alg })
    .setIssuedAt()
    .setExpirationTime("2h")
    .sign(Buffer.from(secret, "base64"));

  const refreshToken = await new jose.SignJWT({
    "account-id": id,
    "account-email": email,
    "token-type": "REFRESH_TOKEN",
  })
    .setProtectedHeader({ alg })
    .setIssuedAt()
    .setExpirationTime("2h")
    .sign(Buffer.from(secret, "base64"));

  setCookie("auth_token", authToken);
  localStorage.setItem("refresh_token", refreshToken);
  localStorage.setItem(
    "auth_token_expire",
    new Date(new Date().setHours(new Date().getHours() + 2)).toISOString()
  );

  router.replace("/dashboard");
  router.refresh();
}

export default function Dev(): JSX.Element {
  const router = useRouter();

  const predefinedBots: Array<{
    id: number;
    email: string;
  }> = [
    { id: 999999999, email: "test1@email" },
    { id: 999999998, email: "test2@email" },
    { id: 999999997, email: "test3@email" },
    { id: 999999996, email: "test4@email" },
    { id: 999999901, email: "test5@email" },
    { id: 10000, email: "0test1@email" },
    { id: 10001, email: "0test2@email" },
    { id: 10002, email: "0test3@email" },
    { id: 10003, email: "0test4@email" },
    { id: 10004, email: "0test5@email" },
  ];

  return (
    <div className="h-full w-full flex justify-center items-center">
      <div className="bg-secondary rounded-xl">
        <form
          className="flex flex-col space-y-4 p-4 justify-center items-center"
          onSubmit={(e) => {
            submitHandler(e, router).catch(() => {});
          }}
        >
          <label>ID:</label>
          <input
            type="number"
            name="id"
            className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
            required
          />
          <label>E-Mail:</label>
          <input
            type="email"
            name="email"
            required
            className="max-xl:h-10 bg-text border border-secondary front-bold text-primary text-lg rounded-lg focus:ring-primary focus:border-primary block p-2.5 w-full"
          />
          <button className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg">
            Set Token
          </button>
        </form>
      </div>
      <div className="ml-5 flex flex-wrap w-1/4">
        {predefinedBots.map((bot, index) => (
          <button
            key={index}
            className="btn bg-primary text-text py-2 w-48 rounded-lg shadow-md text-lg mb-4 ml-4"
            onClick={() => {
              setToken(bot.id, bot.email, router).catch(() => {});
            }}
          >
            Bot: {bot.email.split("@")[0]}
          </button>
        ))}
      </div>
    </div>
  );
}
