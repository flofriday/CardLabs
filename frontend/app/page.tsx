import Image from "next/image";
export default function Home() {
  return (
    <main className="flex justify-around">
      <div className="text-text text-[7vw] font-base w-4/12 pt-[15vh]">
        Elevate
        <br />
        your
        <br />
        strategies
      </div>
      <div className="w-5/12 pt-[5vh]">
        <img src="/landingPageImg.svg" className="w-full" />
      </div>
    </main>
  );
}
