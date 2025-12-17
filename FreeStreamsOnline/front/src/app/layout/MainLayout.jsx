import { Outlet } from "react-router-dom";
import { Footer } from "./Footer";
import { Header } from "./Header";

export const MainLayout = () => {
    return (
        <>
            <Header />
            <main className="main flex-grow-1 p-2">
                <Outlet />
            </main>
            <Footer />
        </>
    );
};
