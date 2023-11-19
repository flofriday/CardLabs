import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import NavBar from "./components/NavBar/NavBar";

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
  // h-[calc(100%_-_5rem)] is needed so that the h-full works as expected in the
  // children
  return (
    <html lang="en">
      <body className={inter.className}>
        <NavBar />
        <div className="h-[calc(100%_-_3.5rem)]">{children}</div>
      </body>
    </html>
  );
}
