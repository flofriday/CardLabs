import Image from "next/image";

export default function Profile() {
  return (
    <div className="w-11">
      <Image
        src="/example_profile_pic.jpg"
        alt="Profile Image"
        className="w-11 shadow rounded-full max-w-full h-auto align-middle border-none"
        width={100}
        height={100}
      />
    </div>
  );
}
