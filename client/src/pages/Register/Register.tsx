import { FormEvent, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import styles from '../Login/Login.module.css';
import { AppDispatch, RootState } from '../../store/store';
import { register, userActions } from '../../store/user.slice';
import Button from '../../components/Button/Button';
import Input from '../../components/Input/Input';

export type RegisterForm = {
	name: {
		value: string;
	};
	email: {
		value: string;
	};
	phone: {
		value: string;
	};
	address: {
		value: string;
	};
	password: {
        value: string;
    };
}

export function Register() {
	const navigate = useNavigate();
	const dispatch = useDispatch<AppDispatch>();
	const { jwt, registerErrorMessage } = useSelector((s: RootState) => s.user);

	useEffect(() => {
		if (jwt) {
			navigate('/');
		}
	}, [jwt, navigate]);

	const submit = async (e: FormEvent) => {
		e.preventDefault();
		dispatch(userActions.clearRegisterError());
		const target = e.target as typeof e.target & RegisterForm;
		const { name, email, phone, address, password} = target;
		dispatch(register({ name: name.value, email: email.value, phone: phone.value, address: address.value, password: password.value }));
	};

	return <div className={styles['login']}>
		{registerErrorMessage && <div className={styles['error']}>{registerErrorMessage}</div>}
		<form className={styles['form']} onSubmit={submit}>
			<div className={styles['field']}>
				<label htmlFor="email">Your email</label>
				<Input id="email" name='email' placeholder='Email'/>
			</div>
			<div className={styles['field']}>
				<label htmlFor="password">Your password</label>
				<Input id="password" name='password' type="password" placeholder='Password'/>
			</div>
			<div className={styles['field']}>
				<label htmlFor="name">Your name</label>
				<Input id="name" name='name' placeholder='Name'/>
			</div>
			<div className={styles['field']}>
				<label htmlFor="phone">Your phone</label>
				<Input id="phone" name='phone' placeholder='Phone'/>
			</div>
			<div className={styles['field']}>
				<label htmlFor="address">Your address</label>
				<Input id="address" name='address' placeholder='Address'/>
			</div>
			<Button appearence="big">Register</Button>
		</form>
		<div className={styles['links']}>
			<div>Do you have an account?</div>
			<Link to="/auth/login">Login</Link>
		</div>
	</div>;
}