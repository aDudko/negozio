import React, {Suspense} from 'react';
import ReactDOM from 'react-dom/client';
import {RouterProvider, createBrowserRouter, defer} from 'react-router-dom';
import { Provider } from "react-redux";
import axios from "axios";
import './index.css';
import {SERVER} from "./helper/variables.ts";
import {store} from "./store/store.ts";
import {Layout} from "./layout/Layout/Layout.tsx";
import {AuthLayout} from "./layout/Auth/AuthLayout.tsx";
import {RequireAuth} from "./helper/RequireAuth.tsx";
import {Login} from "./pages/Login/Login.tsx";
import {Register} from "./pages/Register/Register.tsx";
import {Error as ErrorPage} from "./pages/Error/Error.tsx";
import Menu from "./pages/Menu/Menu.tsx";
import {Success} from "./pages/Success/Success.tsx";
import {Cart} from "./pages/Cart/Cart.tsx";
import {Product} from "./pages/Product/Product.tsx";

const router = createBrowserRouter([
    {
        path: '/',
        element: <RequireAuth><Layout /></RequireAuth>,
        children: [
            {
                path: '/',
                element: <Suspense fallback={<>Loading...</>}><Menu /></Suspense>
            },
            {
                path: '/success',
                element: <Success />
            },
            {
                path: '/cart',
                element: <Cart />
            },
            {
                path: '/product/:id',
                element: <Product />,
                errorElement: <>Error</>,
                loader: async ({ params }) => {
                    const jwt = store.getState().user.jwt
                    return defer({
                        data: new Promise((resolve, reject) => {
                            axios.get(`${SERVER}/product/${params.id}`, {
                                headers: {
                                    Authorization: `Bearer ${jwt}`
                                }
                            })
                            .then(data => resolve(data))
                            .catch(e => reject(e));
                        })
                    });
                }
            }
        ]
    },
    {
        path: '/auth',
        element: <AuthLayout />,
        children: [
            {
                path: 'login',
                element: <Login />
            }, {
                path: 'register',
                element: <Register />
            }
        ]
    },
    {
        path: '*',
        element: <ErrorPage />
    }
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <Provider store={store}>
            <RouterProvider router={router} />
        </Provider>
    </React.StrictMode>
);
