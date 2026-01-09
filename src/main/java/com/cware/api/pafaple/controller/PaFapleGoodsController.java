package com.cware.api.pafaple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cware.framework.core.basic.AbstractController;

import io.swagger.annotations.Api;

@Api(value="/pafaple/goods")
@Controller("com.cware.api.pafaple.PaFapleAsycController")
@RequestMapping(value="/pafaple/goods")
public class PaFapleGoodsController extends AbstractController {

}
