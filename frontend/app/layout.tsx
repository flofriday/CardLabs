import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import NavBar from "./components/NavBar/NavBar";
import Notifications from "./components/Notifications";
import NoSsr from "@/app/components/NoSsr";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Cardlabs",
  description: "A fun app",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}): JSX.Element {
  return (
    <html lang="en">
      <body className={inter.className}>
        <NoSsr>
          <NavBar />
          <div className="h-full pt-14">{children}</div>
          <Notifications />
        </NoSsr>
      </body>
    </html>
  );
}
